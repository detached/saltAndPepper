export function getToken() {
  return localStorage.getItem("SESSION");
}

export function storeToken(sessionToken) {
  return localStorage.setItem("SESSION", sessionToken);
}

export function deleteToken() {
  localStorage.removeItem("SESSION");
}
