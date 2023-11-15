import styled from 'styled-components';

export const Title = styled.h1`
  color: var(--font-rev);
  font-weight: 500;
  padding: 25px 0 0 25px;
  margin-bottom: 30px;
`;

export const DemoArticle = styled.article`
  display: flex;
  flex-wrap: wrap;
  margin: 50px 0;

  @media (max-width: 800px) {
    flex-direction: column;
  }
`;

export const ImgWrapper = styled.div`
  flex: 0 0 320px;
  padding-left: 25px;

  @media (max-width: 800px) {
    flex: auto;
    width: 100%;
  }
`;

export const DemoImg = styled.img`
  border-radius: 0.75rem;
  border: 1px solid var(--border-basic);
  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;
`;

export const Description = styled.div`
  flex: 1 1 320px;
  max-width: 600px;

  color: var(--font-rev);

  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;

  padding: 0 25px;

  @media (max-width: 800px) {
    flex: auto;
    width: 100%;
    text-align: left;
    padding-top: 40px;
  }
`;

export const DivLine = styled.div`
  width: calc(100% - 50px);
  height: 1px;

  margin: 0 25px;

  background-color: var(--border-basic);
`;
