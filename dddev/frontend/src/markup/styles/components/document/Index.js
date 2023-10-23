import styled from 'styled-components';

export const ContentGrid = styled.section`
  position: absolute;

  //수평 수직 중앙정렬
  top: 100px;
  left: 50%;
  transform: translateX(-50%);

  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-auto-rows: 600px;
  justify-items: center;
  align-items: center;

  @media (min-width: 1260px) {
    & > :first-child {
      border-radius: 12px 0 0 12px;

      & > div {
        border-right: 1px solid #fff;
      }
    }
    & > :last-child {
      border-radius: 0 12px 12px 0;

      & > div {
        border-left: 1px solid #fff;
      }
    }
  }

  //화면 넓이가 768px 이하일 때 2열로 변경
  @media (max-width: 1260px) {
    grid-template-columns: repeat(1, 1fr);
    grid-auto-rows: 420px;
    justify-items: center;
    align-items: center;
  }
`;

export const ContentCard = styled.article`
  width: 420px;
  height: 600px;

  background-color: #343434;
`;

export const Content = styled.div`
  overflow-y: auto;
  overflow-x: hidden;
  text-overflow: ellipsis;

  margin: 40px 0;
  padding: 0 20px;
  height: calc(100% - 80px);
`;
