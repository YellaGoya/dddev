const GitLogin = () => {
  // const clientId = 'Iv1.5fdeb1bff3f8eedc';
  // const redirectUri = 'http://localhost:3000/login/github';
  const clientId = 'Iv1.be0efb5e5a6b95fa';
  const redirectUri = 'https://k9d103.p.ssafy.io:8001/login/github';

  const loginUrl = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&scope=user`;
  return (
    <>
      <a href={loginUrl}>Login test!2</a>
      <article>여기 랜딩</article>
    </>
  );
};

export default GitLogin;
