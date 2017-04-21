$(function(){ // on dom ready

var cy = cytoscape({
  container: document.getElementById('graph'),

  layout: {
    name: 'preset',
  },

  style: cytoscape.stylesheet()
    .selector('node')
      .css({
        'shape': 'rectangle',
        'width': '200',
		'height': '70',
        'content': 'data(name)',
        'text-valign': 'center',
        'color': 'black', /* text color */
        'background-color': '#f6f7fe',
        'border-width': '1',
        'border-style': 'solid',
		'border-color': 'black'
      })
    .selector(':selected')
      .css({
        'background-color': '#f8f808'
      })
    .selector('edge')
      .css({
        'curve-style': 'bezier',
        'width': '2',
        'line-style': 'solid',
        'line-color': '#111111',
        'source-arrow-color': '#111111',
        'target-arrow-color': '#111111'
      })
    .selector('edge.composition')
      .css({
        'line-style': 'solid',
        'target-arrow-shape': 'diamond',
        'target-arrow-fill': 'hollow'
      })
    .selector('edge.dependency')
      .css({
        'line-style': 'dashed',
        'target-arrow-shape': 'triangle',
        'target-arrow-fill': 'filled'
      })
    .selector('edge.deployment')
      .css({
        'line-style': 'solid',
        'target-arrow-shape': 'circle',
        'target-arrow-fill': 'hollow'
      })
    .selector('.faded')
      .css({
        'opacity': 0.25,
        'text-opacity': 0
      }),

  elements: {
    nodes: [
      { data: { id: '1', name: 'PicoCMDB - demo', type: 'APPLICATION' }, position: { x: 300, y: 10} },
      { data: { id: '2', name: 'PicoCMDB - test', type: 'APPLICATION' }, position: { x: 10, y: 300} },
      { data: { id: '3', name: 'picoCMDB - demo', type: 'DATABASE' }, position: { x: 10, y: 10} },
      { data: { id: '4', name: 'picoCMDB - test', type: 'DATABASE' }, position: { x: 300, y: 300} }
    ],
    edges: [
      { data: { source: '2', target: '1' }, classes: 'dependency' },
      { data: { source: '2', target: '3' }, classes: 'composition' },
      { data: { source: '4', target: '1' }, classes: 'deployment' }
    ]
  },

  ready: function(){
    window.graph = this;

    // giddy up
  }
});

}); // on dom ready
