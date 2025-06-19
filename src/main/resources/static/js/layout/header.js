// static/js/layout/header.js
import { fetchData } from "../common/fetch-util.js";

document.addEventListener("DOMContentLoaded", async () => {
    const loginBox = document.querySelector("#login_box");
    const userDashboard = document.querySelector("#user_dashboard");
    const usernameSpan = document.querySelector("#username");
    const logoutBtn = document.querySelector("#logout");

if(logoutBtn){
    logoutBtn.addEventListener("click", async () =>{
        await fetch("/api/auth/logout",{
            method: "POST",
            credentials: "include"
        });
        window.location.reload(); // UI 갱신
    })
}
     try {
            const response = await fetch("/api/auth/me", {
                method: "GET",
                credentials: "include" // ✅ 쿠키 자동 전송
            });

            if (!response.ok) throw new Error("인증 실패");

            const result = await response.json();

            // ✅ 로그인 상태 처리
            loginBox.style.display = "none";
            userDashboard.style.display = "block";
            usernameSpan.textContent = result.data.nickname + "님 환영합니다.";
        } catch (err) {
            // ❌ 로그인 안 됨 → 비로그인 UI 노출
            loginBox.style.display = "block";
            userDashboard.style.display = "none";
            console.warn("로그인 안 됨:", err.message);
        }

});
