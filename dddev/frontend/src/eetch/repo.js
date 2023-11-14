import eetch from 'eetch/eetch';

export const repoList = async ({ accessToken, refreshToken }) => {
  const url = `https://k9d103.p.ssafy.io:8001/repository/list`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};
