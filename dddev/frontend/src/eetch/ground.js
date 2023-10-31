export const groundCreate = async ({ Authorization, name, repoId }) => {
  const url = `https://k9d103.p.ssafy.io/ground/${repoId}`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization,
    },
    body: JSON.stringify({ name }),
  };

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  return res.json();
};
