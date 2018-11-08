/* eslint-env jquery */
$('.rfp-tab-content').find('.rfp-tab-pane').each(function(idx, item) { // eslint-disable-line no-unused-vars
    var navTabs = $(this).closest('.rfp-code-tabs').find('.rfp-nav-tabs'),
        title = $(this).attr('title');
    navTabs.append('<li><a href="#">'+title+'</a></li>');
});

$('.rfp-code-tabs ul.rfp-nav-tabs').each(function() {
    $(this).find("li:first").addClass('rfp-active');
})

$('.rfp-code-tabs .rfp-tab-content').each(function() {
    $(this).find("div:first").addClass('rfp-active');
});

$('.rfp-nav-tabs a').click(function(e) {
    e.preventDefault();
    var tab = $(this).parent(),
        tabIndex = tab.index(),
        tabPanel = $(this).closest('.rfp-code-tabs'),
        tabPane = tabPanel.find('.rfp-tab-pane').eq(tabIndex);
    tabPanel.find('.rfp-active').removeClass('rfp-active');
    tab.addClass('rfp-active');
    tabPane.addClass('rfp-active');
});
