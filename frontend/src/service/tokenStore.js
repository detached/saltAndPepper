const accessTokenKey = "TOKEN";
const refreshTokenKey = "REFRESH_TOKEN";

export function storeAccessToken(token) {
  localStorage.setItem(accessTokenKey, token);
}

export function storeRefreshToken(token) {
  localStorage.setItem(refreshTokenKey, token);
}

function getFromLocalStorage(name) {
  let token = localStorage.getItem(name);
  if (token) {
    return token;
  } else {
    return "";
  }
}

export function getAccessToken() {
  return getFromLocalStorage(accessTokenKey);
}

export function getRefreshToken() {
  return getFromLocalStorage(refreshTokenKey);
}

export function removeAccessToken() {
  localStorage.removeItem(accessTokenKey);
}

export function removeRefreshToken() {
  localStorage.removeItem(refreshTokenKey);
}
