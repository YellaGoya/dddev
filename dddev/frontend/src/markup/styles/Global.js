import { createGlobalStyle, styled } from 'styled-components';

export const Global = createGlobalStyle`
  * {
    box-sizing: border-box;
  }

  html {
    font-size: 16px;
  }

  body {
    margin: 0;
    overflow:hidden;
  }

  body, button, input{
    font-family : Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', sans-serif;
  }

  h1, h2, h3, h4, h5, h6 ,ul, li {
    margin: 0;
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

  background-color: pink;
`;

export const ContentWrapper = styled.section`
  position: relative;
  max-width: 960px;

  margin: 0 auto;

  background-color: #777;
`;
