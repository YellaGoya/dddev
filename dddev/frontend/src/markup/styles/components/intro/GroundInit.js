import styled from 'styled-components';

export const GroundWrapper = styled.div`
  color: #fff;
`;

export const GroundDeck = styled.section`
  display: grid;
  justify-items: center;
  align-items: top;
  @media (min-width: 730px) {
    grid-template-columns: repeat(2, 1fr);
    width: 710px;
    // 상단
  }

  @media (max-width: 729px) {
    grid-template-columns: repeat(1, 1fr);
    justify-items: center;
    align-items: center;
  }
`;

export const GroundCard = styled.article`
  width: 355px;
  background-color: gray;
`;

export const Title = styled.h1``;
