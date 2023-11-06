import eetch from 'eetch/eetch';

export const repoList = async ({ accessToken, refreshToken }) => {
  const url = `https://k9d103.p.ssafy.io/repository/list`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
  };

  const res = await eetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  return res.json();
};
