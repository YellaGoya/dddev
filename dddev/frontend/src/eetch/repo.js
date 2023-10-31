export const repoList = async ({ Authorization }) => {
  const url = `https://k9d103.p.ssafy.io/repository/list`;
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
