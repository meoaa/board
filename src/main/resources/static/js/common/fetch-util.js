// static/js/common/fetch-util.js

export async function fetchData(url, method = 'GET', data = null, headers = {}) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
            ...headers
        }
    };
    if (["POST", "PUT", "PATCH"].includes(method)) {
        options.body = JSON.stringify(data);
    }

    const response = await fetch(url, options);
    const result = await response.json();

    if (!response.ok) throw result;
    return result;
}
