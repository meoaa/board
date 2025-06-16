// static/js/layout/header.js
import { fetchData } from "../common/fetch-util.js";

document.addEventListener("DOMContentLoaded", async () => {
    const jwtToken = localStorage.getItem("jwtToken");

    const loginBox = document.querySelector("#login_box");
    const userDashboard = document.querySelector("#user_dashboard");

    if (jwtToken) {
        loginBox.style.display = "none";

        try {
            const payload = await fetchData("/api/members/me", "GET", null, {
                Authorization: `Bearer ${jwtToken}`
            });

            document.querySelector("#username").innerText = `${payload.data.nickname}님 환영합니다.`;

            document.querySelector("#logout").addEventListener("click", () => {
                localStorage.removeItem("jwtToken");
                window.location.href = "/";
            });
        } catch {
            localStorage.removeItem("jwtToken"); // 만료 등 오류 시 제거
            loginBox.style.display = "block";
            userDashboard.style.display = "none";
        }
    } else {
        loginBox.style.display = "block";
        userDashboard.style.display = "none";
    }
});
