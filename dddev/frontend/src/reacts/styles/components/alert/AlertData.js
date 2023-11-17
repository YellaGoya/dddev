import styled from 'styled-components';

export const AlarmList = styled.ul`
  padding: 15px;
  height: fit-content;
  list-style: none;
  border-radius: 0.75rem;
  border: 1px solid var(--border-basic);
`;

export const AlarmItem = styled.li`
  padding: 12px 15px;

  &:hover {
    background-color: var(--bg-rev-hover);
    & > span > span.user {
      color: var(--font-basic);
    }
  }

  border-radius: 0.25rem;

  transition: all 0.2s ease;

  & > span {
    cursor: pointer;
  }
`;

export const AlertTag = styled.span`
  padding: 3px 6px;
  background-color: var(--button-lite);
  border-radius: 0.25rem;
  font-size: 0.9rem;
  margin: 0 5px;
  line-height: 1.8rem;
`;

export const Username = styled.span`
  font-size: 0.9rem;
  font-weight: 700;
  margin-left: 5px;
`;

export const UserMessage = styled.span`
  font-size: 0.9rem;
  font-weight: 500;
`;

export const NoAlarm = styled.li`
  display: flex;
  align-items: center;
  justify-content: center;

  height: 50px;
  opacity: 0.5;
`;
