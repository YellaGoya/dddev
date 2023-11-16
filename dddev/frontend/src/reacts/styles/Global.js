import { createGlobalStyle, styled } from 'styled-components';

export const Global = createGlobalStyle`
  * {
    box-sizing: border-box;
    color: var(--font-rev);

    ::selection {
      background: var(--selection-bg);
      color: var(--selection-font);
    }
  }

  html {
    font-size: 18px;
    overflow-y: hidden;
  }

  body {
    margin: 0;
    background-color: var(--bg-basic);

  }

  body, button, input{
    font-family : Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', sans-serif;
  }

  h1, h2, h3, h4, h5, h6 ,ul, li {
    margin: 0;
  }


  ::-webkit-scrollbar {
    width: 9px;
  }

  ::-webkit-scrollbar-track {
    background: transparent;
  }

  ::-webkit-scrollbar-thumb {
    background-color: var(--font-basic);
    background-clip: padding-box;
    border-right: 3px solid transparent;
    border-top: 73px solid transparent;
    border-bottom: 73px solid transparent;;
    border-left: 3px solid transparent;
  }

  ::-webkit-scrollbar-thumb:hover {
    background-color: var(--font-rev);
    background-clip: padding-box;
    border-right: 3px solid transparent;
    border-top: 73px solid transparent;
    border-bottom: 73px solid transparent;;
    border-left: 3px solid transparent;
  }

  .reactModalOverlayCustom {
    position: fixed;
    left:0;
    top:0;
    width:100%;
    height:100%;
    background-color: rgba(255, 255, 255, 0.05);
    backdrop-filter: blur(3px);
    transition: backdrop-filter 0.3s ease;
  }
`;

export const ViewWrapper = styled.div`
  position: relative;
  z-index: 0;

  display: flex;
  overflow-x: hidden;
  overflow-y: auto;
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
  margin-left: 9px;
  overflow-y: scroll;
  overflow-x: hidden;

  background-color: var(--bg-basic);

  &:hover {
    &::-webkit-scrollbar-thumb {
      background-color: var(--thumb-hover);
    }

    &::-webkit-scrollbar-thumb:hover {
      background-color: var(--font-rev);
    }
  }
`;

export const ContentWrapper = styled.section`
  position: relative;
  max-width: 960px;
  height: calc(100vh - 35px);

  background-color: inherit;

  margin: 0 auto;
`;
