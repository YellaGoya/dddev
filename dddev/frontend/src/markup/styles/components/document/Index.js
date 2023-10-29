import styled from 'styled-components';

export const ContentGrid = styled.section`
  position: absolute;

  //수평 수직 중앙정렬
  top: 100px;
  left: 50%;
  transform: translateX(-50%);

  display: grid;
  justify-items: center;
  align-items: center;

  border-radius: 0.75rem;
  border: 1px solid #30363d;

  overflow: hidden;

  margin: 1px;

  @media (min-width: 1560px) {
    grid-template-columns: repeat(3, 1fr);
    grid-auto-rows: 600px;

    & > :first-child {
      & > div {
        border-right: 1px solid #30363d;
      }
    }
    & > :last-child {
      & > div {
        border-left: 1px solid #30363d;
      }
    }
  }

  @media (max-width: 1559px) {
    grid-template-columns: repeat(1, 1fr);
    justify-items: center;
    align-items: center;
    width: calc(100% - 4px);
    min-width: 361px;
    max-width: 900px;

    & > :first-child {
      & > div {
        border-bottom: 1px solid #30363d;
      }
    }
    & > :last-child {
      & > div {
        border-top: 1px solid #30363d;
      }
    }
  }
`;

export const ContentCard = styled.article`
  position: relative;
  @media (min-width: 1560px) {
    width: 420px;
    height: 600px;
  }

  @media (max-width: 1559px) {
    width: 100%;
    min-width: 361px;
  }

  background-color: #0d1117;
`;

export const ContentLabel = styled.h1`
  position: absolute;
  top: 20px;
  left: 50%;

  color: #fff;
  font-size: 0.9rem;
  font-weight: 300;
  transform: translateX(-50%);
  text-align: center;
`;

export const Content = styled.div`
  overflow-y: auto;
  overflow-x: hidden;
  text-overflow: ellipsis;

  @media (min-width: 1560px) {
    margin: 40px 0;
    padding: 20px 20px 0px 10px;
    height: calc(100% - 80px);
  }

  @media (max-width: 1559px) {
    margin: 0 20px;
    padding: 60px 0 40px 0px;

    & > :first-child {
      padding-left: 0px;
    }
  }
`;

export const TreeParent = styled.ul`
  display: ${({ $toggle }) => ($toggle ? 'none' : 'block')};
  padding-left: 12px;
  list-style: none;
`;

export const TreeName = styled.span`
  position: relative;

  padding: 3px 0px 3px 30px;
  color: #fff;

  display: flex;
  align-items: center;
  cursor: pointer;

  & > svg {
    position: absolute;
    left: 0.3rem;
    top: 0px;
    color: ${({ $toggle }) => ($toggle ? '#fff' : '#ffb938')};
    transform: ${({ $toggle }) => ($toggle ? 'rotate(-90deg)' : 'rotate(0deg)')};
    transition:
      transform 0.4s ease,
      color 0.4s ease;
  }

  &:hover {
    background-color: #000;
    border-radius: 4px;
  }

  & > p {
    font-weight: 300;
    margin: 0;

    &:hover {
      text-decoration: underline;
      text-underline-offset: 3px;
    }
  }
`;

export const TreeChild = styled.li`
  margin: 2px 0;
`;
