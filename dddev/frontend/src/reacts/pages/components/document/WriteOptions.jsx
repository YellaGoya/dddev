import { useEffect } from 'react';

const WriteOptions = () => {
  useEffect(() => {
    console.log('hello');
  }, []);
  return <div>WriteOptions</div>;
};

export default WriteOptions;
