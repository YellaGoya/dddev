export const githubSync = async ({ code }) => {
  const url = `https://k9d103.p.ssafy.io/oauth/sign-in?code=${code}`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  const accessToken = res.headers.get('Authorization');
  const message = await res.text();

  const base64Url = accessToken.split('.')[1];
  const base64 = base64Url.replace('-', '+').replace('_', '/');
  const payload = JSON.parse(window.atob(base64));

  const expirationDate = new Date(payload.exp * 1000);

  return {
    accessToken,
    accessExp: expirationDate,
    refreshToken: res.headers.get('Authorization-Refresh'),
    nickname: res.headers.get('nickname'),
    role: res.headers.get('role'),
    lastGround: res.headers.get('lastGround'),
    message,
  };
};

export const githubTokenRegist = async ({ Authorization, personalAccessToken }) => {
  const url = `https://k9d103.p.ssafy.io/user/personal-access-token`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization,
    },
    body: JSON.stringify({ personalAccessToken }),
  };

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  return res;
};

export const userInfo = async ({ Authorization }) => {
  const url = `https://k9d103.p.ssafy.io/user/user-info`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization,
    },
  };

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  return res.json();
};
