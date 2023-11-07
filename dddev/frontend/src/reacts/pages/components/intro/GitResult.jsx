import { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import eetch from 'eetch/eetch';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { loginUser } from 'redux/actions/user';
import { logoutUser } from 'redux/actions/user';
import Input from 'reacts/pages/components/common/Input';

import demo1 from 'assets/demos/demo1.webp';
import demo2 from 'assets/demos/demo2.webp';
import demo3 from 'assets/demos/demo3.webp';
import demo4 from 'assets/demos/demo4.webp';
import demo5 from 'assets/demos/demo5.webp';

import * as s from 'reacts/styles/components/intro/GitResult';
const GitResult = () => {
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user);
  const [nickname, setNickname] = useState('');
  const [role, setRole] = useState('');

  const [pat, setPat] = useState('');
  const [patMessage, setPatMessage] = useState(null);
  const navigate = useNavigate();

  const submitPat = () => {
    eetch
      .githubTokenRegist({ accessToken: user.accessToken, refreshToken: user.refreshToken, personalAccessToken: pat })
      .then(() => {
        setPatMessage(null);
        navigate('/');
      })
      .catch(() => {
        setPatMessage('틀려먹음');
      });
  };

  useEffect(() => {
    const code = window.location.href.split('=')[1];
    if (code === ``) return;

    eetch
      .githubSync({ code })
      .then((res) => {
        eetch
          .userGrounds({ accessToken: res.accessToken })
          .then((grounds) => {
            const groundsList = grounds.data.map((ground) => ground.groundDto.id);
            const groundsMap = grounds.data.map((ground) => ground.groundDto);
            dispatch(
              loginUser({
                accessToken: res.accessToken,
                accessExp: res.accessExp,
                refreshToken: res.refreshToken,
                lastGround: res.lastGround,
                groundsList,
                groundsMap,
              }),
            );

            setNickname(res.nickname);
            setRole(res.role);

            if (res.role === 'USER') navigate('/');
          })
          .catch((err) => {
            if (err.message === 'RefreshTokenExpired') {
              dispatch(logoutUser());
              dispatch(setMenu(false));
              dispatch(setMessage(false));
              navigate(`/login`);
            }
          });
      })
      .catch((err) => {
        console.log(err);
      });
  }, [dispatch, navigate]);

  return (
    role === 'GUEST' && (
      <>
        <s.Title>
          {nickname}님의 <br />
          Personal Access Token 등록
        </s.Title>
        <Input label="Pat" data={pat} setData={setPat} enter={submitPat} click={submitPat} message={patMessage} />
        <s.DemoArticle>
          <s.ImgWrapper>
            <s.DemoImg src={demo1} />
          </s.ImgWrapper>
          <s.Description>깃헙 사용자 프로필에서 &apos;설정&apos;을 눌러 이동합니다.</s.Description>
        </s.DemoArticle>
        <s.DivLine />
        <s.DemoArticle>
          <s.ImgWrapper>
            <s.DemoImg src={demo2} />
          </s.ImgWrapper>
          <s.Description>설정 페이지의 좌측 카테고리 중 &apos;개발자 설정&apos;을 눌러 이동합니다.</s.Description>
        </s.DemoArticle>
        <s.DivLine />
        <s.DemoArticle>
          <s.ImgWrapper>
            <s.DemoImg src={demo3} />
          </s.ImgWrapper>
          <s.Description>PAT(Personal access tokens)의 classic 토큰을 눌러 이동합니다.</s.Description>
        </s.DemoArticle>
        <s.DivLine />
        <s.DemoArticle>
          <s.ImgWrapper>
            <s.DemoImg src={demo4} />
          </s.ImgWrapper>
          <s.Description>
            &apos;Generate a personal access token&apos;을 눌러 이동합니다. <br />
            이전에 발급한 PAT 토큰이 존재한다면 아래 설명과 같이 수정해주세요.
          </s.Description>
        </s.DemoArticle>
        <s.DivLine />
        <s.DemoArticle>
          <s.ImgWrapper>
            <s.DemoImg src={demo5} />
          </s.ImgWrapper>
          <s.Description>
            PAT 토큰의 이름, 기한을 필요에 따라 설정 후, <br />
            repo 관련 옵션을 모두 허용, 토근을 발급합니다.
          </s.Description>
        </s.DemoArticle>
      </>
    )
  );
};

export default GitResult;
