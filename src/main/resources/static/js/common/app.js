const API_BASE = '/api';

export async function fetchWithAuth(url, options = {}) {
  let res = await fetch(url, {
    ...options,
    credentials: 'include', // ✅ 쿠키 포함 필수
  });

  if (res.status === 401) {
    console.warn('AccessToken 만료됨, refresh 시도 중...');
    const refreshRes = await fetch(`${API_BASE}/auth/refresh`, {
      method: 'POST',
      credentials: 'include',
    });

    if (refreshRes.ok) {
      console.log('accessToken 재발급 성공. 요청 재시도 중...');
      // 다시 원래 요청 재시도
      res = await fetch(url, {
        ...options,
        credentials: 'include',
      });
    } else {
      console.error('refreshToken도 만료됨. 로그인 페이지로 이동.');
      window.location.href = '/login.html'; // ❗ 상황에 맞게 변경
      return;
    }
  }

  return res;
}

//async function getCurrentUser() {
//  const res = await fetchWithAuth(`${API_BASE}/auth/me`);
//  if (!res || res.status !== 200) {
//    console.warn('로그인된 사용자 아님');
//    return null;
//  }
//  return res.json();
//}
//
//async function getPosts() {
//  const res = await fetchWithAuth(`${API_BASE}/posts`);
//  if (res.ok) {
//    const posts = await res.json();
//    renderPostList(posts);
//  } else {
//    console.error('게시글 가져오기 실패');
//  }
//}
//
//function renderPostList(posts) {
//  const ul = document.getElementById('postList');
//  ul.innerHTML = '';
//  posts.forEach(post => {
//    const li = document.createElement('li');
//    li.textContent = post.title;
//    ul.appendChild(li);
//  });
//}

(async () => {
  const user = await getCurrentUser();
  if (user) {
    console.log('로그인된 사용자:', user);
    await getPosts();
  } else {
    alert('로그인이 필요합니다.');
    window.location.href = '/login.html';
  }
})();