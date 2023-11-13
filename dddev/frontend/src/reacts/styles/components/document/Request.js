import styled from 'styled-components';

export const RequestWrapper = styled.div``;

export const RequestListWrapper = styled.div`
  width: 300px;

  & > div {
    margin-left: 5px;
    margin-top: 12px;
    margin-bottom: 0px;
    & > div {
      width: 270px;
      margin: 0;
    }

    & > ul {
      margin: 16px 12.5px 8px; 12.5px;
    }
  }
`;

export const WriterWrapper = styled.div`
  position: absolute;
  left: 285px;
  top: 0;
  width: calc(100% - 285px);
`;

export const RequestList = styled.ul`
  width: 270px;
  padding: 0;
  margin: 0 15px 15px 5px;
  height: fit-content;

  max-height: calc(100vh - 213px);
  overflow-y: scroll;

  list-style: none;

  border-radius: 0.75rem;
  background-color: var(--bg-lite);
  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 4px;
`;
export const RequestItem = styled.li`
  padding: 9px 0 9px 9px;

  cursor: pointer;
`;

export const ItemWrapper = styled.div`
  padding: 5px;
  border-radius: 0.5rem;

  &:hover {
    background-color: var(--bg-basic);
  }
`;

export const ItemAuthor = styled.span`
  font-size: 0.9rem;
`;
export const ItemTitle = styled.h2``;

export const ProfileImage = styled.img`
  width: 90px;
  height: 30px;

  margin-right: 10px;
  border: 1px solid var(--border-dark);

  //비율 유지
  object-fit: cover;

  transition: filter 0.3s ease;
  border-radius: 25px;
`;
