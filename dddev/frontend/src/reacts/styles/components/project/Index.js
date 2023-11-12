import styled from 'styled-components';

export const SprintWrapper = styled.div``;

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
    right: 38px;

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
    right: 9px;

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
