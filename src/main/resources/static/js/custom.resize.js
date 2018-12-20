$(document).ready( setGraphHeight() );

function setGraphHeight() {
    var pageWrapperDecoHeight = $('#page-wrapper').outerHeight(true) - $('#page-wrapper').height();

    var gwwrHeight = $(window).height() - $('#common-navbar-wrapper-row').outerHeight(true) - pageWrapperDecoHeight;
    //$('#graph-workspace-wrapper-row').outerHeight(gwwrHeight);
};