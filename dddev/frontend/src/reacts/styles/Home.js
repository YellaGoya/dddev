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

      margin: 0 auto;
    }
  }
`;

export const ChartWrapper = styled.section`
  @media (min-width: 1200px) {
    left: 0;
    width: 500px;
  }
`;

export const TimeGrid = styled.article`
  position: realative;
  margin-top: 15px;

  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  grid-template-rows: auto;

  gap: 15px;
`;

export const TimeCard = styled.div`
  position: relative;
  height: fit-content;
  background-color: var(--bg-lite);
  border: 1px solid var(--border-basic);
  border-radius: 0.75rem;

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;
`;

export const TimeCardTitle = styled.h1`
  font-size: 1rem;
  margin-left: 15px;
  margin-top: 15px;
`;

export const TimeCardInfo = styled.p`
  margin: 0;
  margin-top: 15px;
  margin-left: 15px;
  opacity: 0.8;
`;

export const TimeCardDesc = styled.p`
  margin: 0;
  margin-left: 15px;
  margin-bottom: 15px;
  font-weight: 700;
  font-size: 1.2rem;

  color: var(--font-rev);
`;

export const TimeCardPieWrapper = styled.div`
  position: absolute;
  top: calc(50% - 13px);
  right: 7px;
  transform: translateY(-50%);
  width: 50px;
  height: 50px;
`;

export const TimeCardPiePercent = styled.div`
  text-align: center;
  margin-top: 8px;

  font-size: 0.8rem;
  opacity: 0.5;
`;

export const BurnDownWrapper = styled.article`
  margin-top: 15px;
  width: 100%;
  height: 400px;

  background-color: var(--bg-lite);
  border: 1px solid var(--border-basic);
  border-radius: 0.75rem;

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;
`;

export const BurnDownSizeWrapper = styled.div`
  width: 100%;
  height: 350px;
`;

export const BurnDownTitle = styled.h1`
  font-size: 1rem;
  margin-left: 15px;
  margin-top: 15px;
`;

export const IssueCountWrapper = styled.div`
  position: relative;
  margin-top: 15px;
  width: 100%;
  height: 85px;

  background-color: var(--bg-lite);
  border: 1px solid var(--border-basic);
  border-radius: 0.75rem;

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;
`;

export const IssueCountTitle = styled.h1`
  font-size: 1rem;
  margin-left: 15px;
  margin-top: 15px;
`;

export const IssueCountCard = styled.div`
  position: absolute;
  top: 10px;
  right: 30px;
  width: fit-content;
  height: 55px;
`;

export const IssueCountLabel = styled.span`
  margin-left: 10px;
  font-size: 0.9rem;
  opacity: 0.5;
`;

export const IssueHighLight = styled.span`
  position: relative;
  font-size: 2rem;
  font-weight: 700;
  margin-left: 15px;
`;
export const IssueText = styled.div`
  position: absolute;
  width: 100px;
  left: 50%;
  bottom: -15px;
  transform: translateX(-50%);

  text-align: center;
  font-size: 0.8rem;
  opacity: 0.5;
`;

export const DocsWrapper = styled.section`
  @media (min-width: 1200px) {
    right: 0;
    width: calc(100% - 500px);
  }

  background-color: lightblue;
`;
