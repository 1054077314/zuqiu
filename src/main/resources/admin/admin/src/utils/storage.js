const PREFIX = 'admin:'

const storage = {
    key(key) {
        return `${PREFIX}${key}`
    },
    set(key, value) {
        localStorage.setItem(this.key(key), JSON.stringify(value));
    },
    get(key) {
        const value = localStorage.getItem(this.key(key));
        return value ? value.replace(/"/g, '') : "";
    },
    getObj(key) {
        const value = localStorage.getItem(this.key(key));
        return value ? JSON.parse(value) : null;
    },
    remove(key) {
        localStorage.removeItem(this.key(key));
    },
    clear() {
        Object.keys(localStorage)
            .filter(key => key.indexOf(PREFIX) === 0)
            .forEach(key => localStorage.removeItem(key));
    }
}
export default storage;
