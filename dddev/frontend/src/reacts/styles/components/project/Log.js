import styled from 'styled-components';

export const LogWrapper = styled.div`
  min-width: 340px;
  max-width: 960px;
  padding-top: 30px;
  margin: 0 auto;
`;

export const LogInstructionWrapper = styled.article`
  position: relative;
  width: 100%;
  padding: 10px;
  height: ${({ $toggle }) => ($toggle ? 'fit-content' : '53px')};

  overflow: hidden;

  border-radius: 0.75rem;
  border: 1px solid var(--border-basic);

  /* transition: height 0.4s ease; */
  background-color: var(--bg-rev);

  & > h2 {
    position: relative;
    padding-left: 5px;
    font-size: 0.9rem;
    font-weight: 500;
    color: var(--font-basic);

    margin-top: 20px;
    margin-bottom: 10px;
  }

  pre {
    font-size: 0.9rem;
    font-family: 'consolas', 'gulim';
    width: auto;
    margin: 0px 10px 15px 10px;

    overflow-x: auto;

    padding: 30px 20px 30px 20px;
    border-radius: 0.25rem;
    background-color: var(--bg-rev-hover);
    color: var(--font-lite);

    &::-webkit-scrollbar {
      height: 9px;
    }
    &::-webkit-scrollbar-thumb {
      background-clip: padding-box;
      border-left: 20px solid transparent;
      border-right: 20px solid transparent;
      border-top: 3px solid transparent;
      border-bottom: 3px solid transparent;
    }
  }

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;
`;

export const LogTitle = styled.span`
  font-size: 1rem;
  font-weight: 700;

  display: flex;
  align-items: center;

  cursor: pointer;

  padding: 5px 10px;
  border-radius: 0.5rem;
  margin-bottom: 30px;

  color: var(--font-basic);
  &:hover {
    background-color: var(--bg-rev-hover);
  }

  & > svg {
    position: absolute;
    right: 10px;
    fill: var(--font-rev);

    transform: ${({ $toggle }) => ($toggle ? 'rotate(180deg)' : 'rotate(360deg)')};
    fill: ${({ $toggle }) => ($toggle ? 'var(--font-toggle)' : 'var(--font-rev)')};

    transition: all 0.4s ease;
    pointer-events: none;
  }
`;

export const GenerateButton = styled.button`
  left: 185px;
  top: -6px;
  position: absolute;
  display: flex;
  align-items: center;

  background-color: var(--bg-lite);
  padding: 5px 10px;

  border-radius: 0.5rem;
  border: none;

  &:hover {
    background-color: var(--bg-lite-hover);
  }

  cursor: pointer;
  & > svg {
    width: 21px;
    height: 21px;
    margin-right: 3px;
  }
`;

export const LogListWrapper = styled.article`
  margin-top: 15px;

  width: 100%;
  background-color: var(--bg-dark);

  border-radius: 0.75rem;

  padding: 15px;

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;
`;

export const LogListTitle = styled.h1`
  font-size: 1.1rem;
  font-weight: 700;
  margin-bottom: 30px;
`;

export const LogList = styled.ul`
  list-style: none;
  padding: 0;
  max-height: 500px;
  overflow-y: auto;

  & > li:nth-child(even) {
    background-color: var(--bg-basic);
  }
`;

export const LogItem = styled.li`
  width: calc(100%);
  padding: 5px 10px;
  border-radius: 0.25rem;
  &.load-more {
    //내부 컨텐츠 수직으로 1줄 정렬
    display: flex;
    align-items: center;
    justify-content: center;

    height: 80px;

    background-color: var(--bg-dark);
  }
`;

export const LogMoreButton = styled.button`
  padding: 10px 12px 5px 12px;
  border: none;
  background-color: transparent;
  cursor: pointer;

  border-radius: 0.5rem;
  transition: background-color 0.2s ease;

  &:hover {
    background-color: var(--bg-lite-hover);
  }

  & > p {
    margin: 0;
  }
`;
