export function storeToken(token) {
  localStorage.setItem("TOKEN", token);
}

export function getToken() {
  let token = localStorage.getItem("TOKEN");
  if (token) {
    return token;
  } else {
    return "";
  }
}

export function removeToken() {
  localStorage.removeItem("TOKEN");
}
