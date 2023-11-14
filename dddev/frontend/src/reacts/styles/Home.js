import styled from 'styled-components';

export const DashWrapper = styled.div`
  position: relative;
  width: 100%;
  height: 100%;

  & > section {
    @media (min-width: 1200px) {
      position: absolute;
      height: 100%;
    }
    @media (max-width: 1199px) {
      position: relative;
      width: 100%;
      max-width: 910px;
      height: 300px;

      margin: 0 auto;
    }
  }
`;

export const ChartWrapper = styled.section`
  @media (min-width: 1200px) {
    left: 0;
    width: 550px;
  }

  background-color: beige;
`;

export const DocsWrapper = styled.section`
  @media (min-width: 1200px) {
    right: 0;
    width: calc(100% - 550px);
  }

  background-color: lightblue;
`;
