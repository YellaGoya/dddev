import styled from 'styled-components';

export const ContentGrid = styled.section`
  position: absolute;

  top: 100px;
  left: 50%;
  transform: translateX(-50%);

  display: grid;
  justify-items: center;
  align-items: center;

  border-radius: 0.75rem;
  border: 1px solid var(--border-basic);

  overflow: hidden;

  margin: 1px;

  @media (min-width: 1560px) {
    grid-template-columns: repeat(3, 1fr);
    grid-auto-rows: 600px;

    & > :first-child {
      & > div {
        border-right: 1px solid var(--border-basic);
      }
    }
    & > :last-child {
      & > div {
        border-left: 1px solid var(--border-basic);
      }
    }
  }

  @media (min-width: 1560px) and (min-height: 691px) {
    top: calc((100vh - 630px) / 2);
  }
  // 화면 넓이 1200px 이하, 높이 500px 이하일 때 top: 20px
  @media (min-width: 1560px) and (max-height: 690px) {
    top: 30px;
  }

  @media (max-width: 1559px) {
    top: 30px;
    grid-template-columns: repeat(1, 1fr);
    justify-items: center;
    align-items: center;
    width: calc(100% - 4px);
    min-width: 361px;
    max-width: 900px;

    & > :first-child {
      & > div {
        border-bottom: 1px solid var(--border-basic);
      }
    }
    & > :last-child {
      & > div {
        border-top: 1px solid var(--border-basic);
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

  background-color: var(--bg-basic);
`;

export const ContentLabel = styled.h1`
  position: absolute;
  top: 20px;
  left: 50%;

  color: var(--font-rev);
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

export const TreeChild = styled.li`
  position: relative;
  margin: 2px 0;
`;

export const TitleWrapper = styled.div`
  position: relative;
  width: fit-content;
  height: 21px;

  max-width: calc(100% - 97px);
`;

export const DocTitle = styled.p`
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: auto;

  height: 21px;
  margin: 0;
`;

export const DocEdit = styled.input`
  position: absolute;
  left: 28px;
  top: 0;

  border: none;
  outline: none;
  color: var(--font-on-edit);
  font-size: 18px;
  font-weight: 300;
  background-color: transparent;

  display: ${({ $onEdit }) => ($onEdit ? 'block' : 'none')};

  width: calc(100% - 100px);
  height: 27px;
`;

export const TreeName = styled.div`
  position: relative;
  width: 100%;

  padding: 3px 0px 3px 30px;

  display: flex;
  align-items: center;
  overflow: hidden;
  cursor: pointer;

  & > .foldSign {
    position: absolute;
    left: 0.3rem;
    top: 0px;
    fill: ${({ $toggle }) => ($toggle ? 'var(--font-rev)' : 'var(--font-toggle)')};
    transform: ${({ $toggle }) => ($toggle ? 'rotate(-90deg)' : 'rotate(0deg)')};
    transition:
      transform 0.4s ease,
      fill 0.4s ease;
  }

  svg {
    fill: var(--font-editor-placeholder);
    &:hover {
      fill: var(--font-rev);

      transition:
        fill 0.2s ease,
        top 0.3s ease,
        opacity 0.2s ease;
    }
  }

  &:hover {
    background-color: var(--bg-black);
    border-radius: 4px;

    .moreButton,
    .editName,
    .deleteDoc {
      opacity: 1;
    }

    .addChild {
      opacity: 1;
      display: ${({ $onEdit }) => ($onEdit ? 'hide' : 'show')};
    }
  }

  .moreButton {
    position: absolute;
    right: 9px;

    width: 23px;
    height: 23px;

    fill: var(--font-editor-placeholder);
    opacity: 0;
    display: ${({ $isMore }) => ($isMore ? 'none' : 'block')};

    transition: opacity 0.2s ease;
    &:hover {
      fill: var(--font-rev);
    }
  }

  .editName {
    position: absolute;
    right: 9px;

    width: 23px;
    height: 23px;

    display: block;
    fill: var(--font-editor-placeholder);

    opacity: 0;
    transition:
      opacity 0.2s ease,
      fill 0.2s ease;

    &:hover {
      fill: var(--font-rev);
    }

    display: ${({ $isMore }) => ($isMore ? 'block' : 'none')};
  }

  .deleteDoc {
    position: absolute;
    right: 38px;

    width: 21px;
    height: 21px;

    display: block;
    fill: var(--font-editor-placeholder);

    opacity: 0;
    transition:
      opacity 0.2s ease,
      fill 0.2s ease;

    display: ${({ $isMore }) => ($isMore ? 'block' : 'none')};

    &:hover {
      fill: var(--font-delete);
    }
  }

  .addChild {
    position: absolute;
    width: 21px;
    height: 21px;

    display: ${({ $onEdit }) => ($onEdit ? 'none' : 'block')};

    right: -1.5rem;
    top: 0;

    fill: var(--font-editor-placeholder);
    opacity: 0;
    transition: opacity 0.2s ease;

    &:hover {
      fill: var(--font-rev);
    }
  }

  & > div > p {
    position: relative;
    font-weight: 300;
    margin: 0;

    color: ${(props) => (props.$isEmpty ? (props.$isNew ? 'var(--font-new)' : 'var(--font-editor-placeholder)') : 'var(--font-rev)')};
    opacity: ${({ $onEdit }) => ($onEdit ? '0' : '1')};
    transition: color 0.2s ease;

    &:hover {
      text-decoration: underline;
      text-underline-offset: 3px;

      color: var(--font-rev);
    }
  }
`;
