import eetch from 'eetch/eetch';

export const groundCreate = async ({ accessToken, refreshToken, name, repoId }) => {
  const url = `https://k9d103.p.ssafy.io/ground/${repoId}`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
    body: JSON.stringify({ name }),
  };

  const res = await eetch(url, options);

  return res.json();
};

export const groundInfo = async ({ accessToken, refreshToken, groundId }) => {
  const url = `https://k9d103.p.ssafy.io/ground/${groundId}`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
  };

  const res = await eetch(url, options);

  return res.json();
};
