// static/js/auth/login.js
import { fetchData } from "../common/fetch-util.js";

document.addEventListener("DOMContentLoaded", () => {
    const loginBtn = document.querySelector("#login_btn");
    if (!loginBtn) return;

    loginBtn.addEventListener("click", async () => {
        const username = document.querySelector("input[name='username']").value;
        const password = document.querySelector("input[name='password']").value;

        try {
            const payload = await fetchData("/api/auth/login", "POST", { username, password });

            console.log(payload);

            window.location.href = "/";
        } catch (err) {
            const errorBox = document.querySelector(".error_box");
            errorBox.innerHTML = `<span>${err.message}</span>`;
        }
    });
});
