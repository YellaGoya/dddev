import styled from 'styled-components';

export const DashWrapper = styled.div`
  position: relative;
  width: 100%;
  max-width: 1410px;
  height: calc(100vh - 35px);

  margin: 0 auto;

  & > section {
    @media (min-width: 1229px) {
      position: absolute;
      height: 100%;

      max-width: 910px;
    }
    @media (max-width: 1228px) {
      position: relative;
      width: 100%;
      max-width: 910px;

      margin: 0 auto;
    }
  }
`;

export const ChartWrapper = styled.section`
  @media (min-width: 1229px) {
    left: 0;
    width: 500px;
    height: 100vh;
    overflow-y: scroll;
  }

  &:hover {
    // ul의 스크롤바 색상 파란색으로
    &::-webkit-scrollbar-thumb {
      background-color: var(--thumb-hover);
    }

    &::-webkit-scrollbar-thumb:hover {
      background-color: var(--font-rev);
    }
  }

  padding-top: 15px;
  padding-bottom: 15px;
`;

export const ChartTitle = styled.h1`
  margin-top: 10px;
  margin-left: 2px;
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
  padding-top: 30px;
  background-color: var(--bg-lite);
  border: 1px solid var(--border-basic);
  border-radius: 0.75rem;

  overflow: hidden;

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;
`;

export const TimeCardTitle = styled.h1`
  position: absolute;
  font-size: 1rem;
  left: 15px;
  top: 15px;
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
  position: relative;
  padding-top: 30px;
  margin-top: 15px;
  width: 100%;
  height: 400px;

  overflow: hidden;

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
  position: absolute;
  font-size: 1rem;
  left: 15px;
  top: 15px;
`;

export const IssueCountWrapper = styled.div`
  position: relative;
  margin-top: 15px;
  width: 100%;
  height: 85px;

  overflow: hidden;

  background-color: var(--bg-lite);
  border: 1px solid var(--border-basic);
  border-radius: 0.75rem;

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;
`;

export const IssueCountTitle = styled.h1`
  position: absolute;
  font-size: 1rem;
  left: 15px;
  top: 15px;
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

export const LinkWrapper = styled.div`
  position: relative;
  margin-top: 15px;
  width: 100%;
  height: fit-content;

  & > div {
    width: calc(50% - 7px);
    height: 85px;

    background-color: var(--bg-lite);
    border: 1px solid var(--border-basic);
    border-radius: 0.75rem;

    display: flex;
    align-items: center;
    justify-content: center;

    font-size: 1rem;
    font-weight: 700;

    box-shadow:
      rgba(0, 0, 0, 0.24) 0px 1px 1px,
      rgba(0, 0, 0, 0.24) 0px 2px 2px,
      rgba(0, 0, 0, 0.24) 0px 4px 6px;

    transition: background-color 0.2s ease;

    cursor: pointer;

    &:hover {
      background-color: var(--bg-lite-hover);
    }

    & > svg {
      width: 23px;
      height: 23px;

      margin-left: 5px;
    }
  }
`;

export const LinkSprint = styled.div``;

export const LinkIssue = styled.div`
  position: absolute;
  top: 0;
  right: 0;
`;

export const NoContentBlur = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  backdrop-filter: blur(10px);
`;

export const NoContentInfo = styled.div`
  position: absolute;

  top: 50%;
  left: 50%;
  transform: translateX(-50%);
  opacity: 0.65;
`;

export const DocsWrapper = styled.section`
  @media (min-width: 1229px) {
    left: 500px;
    width: calc(100% - 500px);
    padding-left: 21px;
    height: calc(100vh - 35px);
    overflow-y: auto;
  }

  /* background-color: lightblue; */
`;

export const RequestWrapper = styled.div`
  width: 100%;
  border-radius: 0.75rem;
  height: fit-content;
  padding-bottom: 30px;
  @media (min-width: 1229px) {
    margin-top: 30px;
  }
`;

export const RequestTitle = styled.h1`
  margin-bottom: 10px;
  margin-left: 2px;

  @media (min-width: 1229px) {
    margin-top: 10px;
  }
`;

export const RequestGrid = styled.article`
  position: realative;

  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  grid-template-rows: auto;

  gap: 15px;
`;

export const RequestCard = styled.div`
  position: relative;
  height: 233px;
  background-color: var(--bg-basic);

  border: 1px solid var(--border-basic);
  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;

  border-radius: 0.75rem;

  transition: background-color 0.2s ease;

  &:hover {
    background-color: var(--bg-lite);
  }

  &.no-request {
    //내부 svg 중앙으로
    height: 150px;
    & > svg {
      width: 48px;
      height: 48px;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
  }

  cursor: pointer;
`;

export const RequestStatus = styled.div`
  margin: 10px 0 0 10px;
  padding: 3px 9px;
  font-size: 0.8rem;
  width: fit-content;

  border-radius: 0.25rem;

  background-color: ${(props) => (props.status === 0 ? 'var(--button-lite)' : props.status === 1 ? 'var(--font-new)' : 'var(--font-toggle)')};
`;

export const RequestName = styled.h1`
  max-height: 80px;
  margin: 0;
  margin-top: 10px;
  margin-left: 10px;
  margin-right: 5px;
  font-size: 1.2rem;
  font-weight: 700;
  color: var(--font-rev);
  overflow: hidden;
  text-overflow: ellipsis;
`;

export const RequestDate = styled.p`
  margin: 5px 0;
  padding: 0 10px;
  text-align: left;
  font-size: 0.8rem;
  font-weight: 400;
`;

export const RequestUserName = styled.h3`
  position: absolute;
  right: 10px;
  bottom: 30px;
`;

export const RequestUserEmail = styled.p`
  position: absolute;
  bottom: 0px;
  right: 10px;
  font-size: 0.8rem;
  opacity: 0.5;
`;

export const IssueWrapper = styled.div``;

export const DivLine = styled.div`
  @media (min-width: 1229px) {
    display: none;
  }
  width: calc(100% - 40px);
  max-width: 900px;
  margin: 0 auto;
  margin-top: 15px;
  margin-bottom: 25px;
  height: 1px;

  background-color: var(--border-basic);
`;

export const AlarmTitle = styled.h1`
  margin-bottom: 10px;
  margin-left: 2px;

  @media (min-width: 1229px) {
    margin-top: 10px;
  }
`;

export const AlarmWrapper = styled.div`
  width: 100%;
  border-radius: 0.75rem;
  height: fit-content;
  padding-bottom: 30px;
`;
