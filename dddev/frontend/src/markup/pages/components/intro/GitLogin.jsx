const GitLogin = () => {
  const clientId = 'Iv1.5fdeb1bff3f8eedc';
  const redirectUri = 'http://192.168.100.63:3000/login/github';

  const loginUrl = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&scope=user`;
  return <a href={loginUrl}>Login test!2</a>;
};

export default GitLogin;
