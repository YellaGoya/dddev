import { ResponsivePie } from '@nivo/pie';

const PieChart = ({ data, endAngle }) => (
  <ResponsivePie
    data={data}
    margin={{ top: 0, right: 0, bottom: 0, left: 0 }}
    innerRadius={0.5}
    padAngle={0.7}
    endAngle={endAngle ? endAngle : 360}
    cornerRadius={3}
    activeOuterRadiusOffset={8}
    colors={{ scheme: 'blues' }}
    borderWidth={1}
    borderColor={{
      from: 'color',
      modifiers: [['darker', 0.2]],
    }}
    enableArcLinkLabels={false}
    arcLinkLabelsSkipAngle={10}
    arcLinkLabelsTextColor="#333333"
    arcLinkLabelsThickness={2}
    arcLinkLabelsColor={{ from: 'color' }}
    enableArcLabels={false}
    arcLabelsSkipAngle={10}
    arcLabelsTextColor={{
      from: 'color',
      modifiers: [['darker', 2]],
    }}
    isInteractive={false}
  />
);

export default PieChart;
