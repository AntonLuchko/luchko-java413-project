window.addEventListener("beforeunload", () => {
    // Пример: localStorage
    const tokenAdmin = "tokenAdmin";
    const tokenUser = "tokenUser";
    for (let i = localStorage.length - 1; i >= 0; i--) {
        const key = localStorage.key(i);
        if (key.startsWith(tokenAdmin) || key.startsWith(tokenUser)) {
            localStorage.removeItem(key);
        }
    }

});
