const base = {
    get() {
        const protocol = window.location.protocol;
        const host = window.location.host;
        const contextPath = '/zuqiujulebguanli';
        return {
            url: protocol + '//' + host + contextPath + '/',
            name: 'zuqiujulebguanli',
            indexUrl: protocol + '//' + host + contextPath + '/front/index.html'
        };
    }
}

export default base
