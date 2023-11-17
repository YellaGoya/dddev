import * as s from 'reacts/styles/components/intro/GitLogin';
// const clientId = 'Iv1.5fdeb1bff3f8eedc';
// const redirectUri = 'http://localhost:3000/login/github';
import LoginBi from 'assets/loginBi.png';
const clientId = 'Iv1.be0efb5e5a6b95fa';
const redirectUri = 'https://k9d103.p.ssafy.io/login/github';

const loginUrl = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&scope=user`;
import GitHubIcon from '@mui/icons-material/GitHub';
const GitLogin = () => {
  return (
    <>
      <s.BiImage src={LoginBi} />
      <s.FlightWrapper>
        <s.DescWrapper>
          <h1>dddev</h1>
          <p>때로 모여 데브. 당신도 가능하다. 협업.</p>
          <p className="powered">powered by ssafy d103 team, github.</p>
        </s.DescWrapper>
        <s.GithubLoginButton
          onClick={() => {
            location.href = loginUrl;
          }}
        >
          <GitHubIcon />
          GitHub 로그인
        </s.GithubLoginButton>
      </s.FlightWrapper>
    </>
  );
};

export default GitLogin;
