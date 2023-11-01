import { createGlobalStyle, styled } from 'styled-components';

export const Global = createGlobalStyle`
  * {
    box-sizing: border-box;
    color: #fff;
  }

  html {
    font-size: 18px;
  }

  body {
    margin: 0;
    overflow:hidden;
    background-color: #0d1117;
  }

  body, button, input{
    font-family : Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', sans-serif;
  }

  h1, h2, h3, h4, h5, h6 ,ul, li {
    margin: 0;
  }


  //스크롤바 오른쪽에서 3px 띄우기
  ::-webkit-scrollbar {
    width: 9px;
  }

  ::-webkit-scrollbar-track {
    background: transparent;
  }

  ::-webkit-scrollbar-thumb {
    background-color: #000;
    background-clip: padding-box;
    border-right: 5px solid transparent;
    border-top: 20px solid transparent;
    border-bottom: 20px solid transparent;;
  }

  ::-webkit-scrollbar-thumb:hover {
    background-color: #fff;
    background-clip: padding-box;
    border-right: 5px solid transparent;
    border-top: 20px solid transparent;
    border-bottom: 20px solid transparent;
  }
`;

export const ViewWrapper = styled.div`
  position: relative;
  z-index: 0;

  display: flex;
  flex-direction: row;
  width: 100%;
  height: ${({ $isLoggedIn }) => ($isLoggedIn ? 'calc(100vh - 35px)' : '100vh')};

  transition: height 0.3s ease;
`;

export const Main = styled.main`
  position: relative;
  z-index: 1;

  flex-grow: 1;
  min-width: 280px;
  @media (min-width: 600px) {
    margin-left: 9px;
    overflow-y: scroll;
  }

  @media (max-width: 599px) {
    margin-left: 0;
    overflow-y: auto;
  }

  overflow-x: hidden;

  background-color: #0d1117;
`;

export const ContentWrapper = styled.section`
  position: relative;
  max-width: 960px;
  min-height: calc(100vh - 35px);

  margin: 0 auto;
  background-color: #0d1117;
`;
