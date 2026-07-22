#!/usr/bin/env python3
import json
from pathlib import Path


def asks_for_latest_one(message: str) -> bool:
    return any(k in message for k in ("最新", "最近", "第一条", "刚发布", "刚录入"))


def asks_contract_unavailable_field(message: str) -> bool:
    return any(k in message for k in ("到期", "薪资", "工资", "期限", "多少钱"))


def format_player_candidates(candidates):
    lines = []
    for i, row in enumerate(candidates, start=1):
        lines.append(
            f"{i}. {row.get('name', '未命名球员')}，编号：{row.get('number', '暂无')}，账号：{row.get('account', '暂无')}"
        )
    return "\n".join(lines)


def format_records(module, records, payload, user_message):
    parts = []
    for i, row in enumerate(records, start=1):
        if module == "球员档案":
            parts.append(
                f"{i}. {row.get('name', '未命名球员')}，编号：{row.get('number', '暂无')}，账号：{row.get('account', '暂无')}"
            )
        elif module == "公告信息":
            parts.append(f"{i}. {row.get('title', '未命名公告')}")
        elif module == "赛事信息":
            parts.append(f"{i}. {row.get('title', '未命名赛事')}")
        elif module == "训练计划":
            parts.append(f"{i}. {row.get('title', '未命名训练计划')}")
        elif module == "合同信息":
            parts.append(f"{i}. {row.get('title', '未命名合同')}")
        elif module == "球员数据":
            parts.append(f"{i}. {row.get('title', '未命名球员数据')}")
    body = "\n".join(parts)
    if module == "合同信息" and asks_contract_unavailable_field(user_message):
        body += "\n说明：当前合同表未维护合同到期时间、薪资或工资字段，不能从系统数据库返回这些信息。"
    return body


def format_empty_contract_reply(user_message, payload, keyword):
    if payload.get("playerMissing") and keyword != "无":
        suffix = "到期时间" if asks_contract_unavailable_field(user_message) else "信息"
        return (
            f"系统里没有找到名为“{keyword}”的球员，无法查询他的合同{suffix}。"
            "请先确认球员姓名，或先用“查看球员档案”查系统里的球员。"
        )
    candidates = payload.get("playerCandidates") or []
    if payload.get("playerAmbiguous") and candidates:
        return (
            "系统里匹配到多个可能的球员，请按姓名或编号再问一次：\n"
            + format_player_candidates(candidates)
        )
    if asks_contract_unavailable_field(user_message):
        tail = "。" if keyword == "无" else f"，关键词：{keyword}。"
        return (
            "系统内未查到匹配的合同信息"
            + tail
            + "另外当前合同表未维护合同到期时间、薪资或工资字段，不能从系统数据库返回这些信息。"
        )
    tail = "。" if keyword == "无" else f"，关键词：{keyword}。"
    return "系统内未查到匹配的合同信息" + tail


def format_single(user_message, payload):
    module = payload["module"]
    keyword = payload.get("keyword", "无")
    records = payload.get("records") or []
    count = len(records)
    if count == 0:
        if module == "合同信息":
            return format_empty_contract_reply(user_message, payload, keyword)
        if keyword == "无":
            return f"系统内未查到匹配的{module}。"
        return f"系统内未查到匹配的{module}，关键词：{keyword}。"
    if count > 1 and keyword != "无":
        return (
            f"系统内匹配到多条{module}，请根据名称或编号进一步指定：\n"
            + format_records(module, records, payload, user_message)
        )
    if asks_for_latest_one(user_message) and count == 1:
        return f"最新{module}：\n" + format_records(module, records, payload, user_message)
    return f"系统查询到{count}条{module}：\n" + format_records(module, records, payload, user_message)


def looks_like_system_data_question(message: str) -> bool:
    return any(
        k in message
        for k in ("球员", "公告", "赛事", "比赛", "训练", "合同", "数据", "档案", "系统内")
    )


def main():
    path = Path(__file__).resolve().parents[1] / "src/test/resources/ai/eval-dataset.json"
    cases = json.loads(path.read_text(encoding="utf-8"))
    print(f"total cases: {len(cases)}")
    failures = []
    for case in cases:
        case_id = case["id"]
        question = case["question"]
        payload = case.get("offlinePayload")
        if payload is not None:
            reply = format_single(question, payload)
            for must in case.get("replyMustContain") or []:
                if must not in reply:
                    failures.append((case_id, f"missing mustContain: {must}", reply[:160]))
            for must_not in case.get("replyMustNotContain") or []:
                if must_not in reply:
                    failures.append((case_id, f"contains mustNotContain: {must_not}", reply[:160]))
        if case.get("expectedFallbackTool") and not looks_like_system_data_question(question):
            failures.append((case_id, "fallback question not recognized", question))
    if failures:
        print(f"failures: {len(failures)}")
        for item in failures:
            print(item)
        raise SystemExit(1)
    print("all offline assertions passed")


if __name__ == "__main__":
    main()
