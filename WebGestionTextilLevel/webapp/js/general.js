$.urlParam = function(name){
    var results = new RegExp('[\\?&amp;]' + name + '=([^&amp;#]*)').exec(window.location.href);
    return results != null && results.length > 0?results[1] || 0:0;
}

/*
hot-sneaks
redmond
humanity
swanky-purse
blitzer
eggplant
smoothness
excite-bike
pepper-grinder
sunny
ui-lightness
cupertino
flick
mint-choc
trontastic
showcase" selected="selected
overcast
dark-hive
vader
black-tie
start
south-street
dot-luv
le-frog
ui-darkness
 * */

function changeTheme(newTheme) {
	if (newTheme=='showcase')
		themePathPrefix = "themes/";
	else {
		themeHref = $('#jquery_theme_link').attr('href');
		if ($("#google").attr('checked')) {
			if (themeHref.indexOf("http:")==0) {
				prefixIndex = themeHref.indexOf("themes/")+7;
				themePathPrefix=themeHref.substring(0,prefixIndex);
			}
			else {
				$("#themeform").submit();
				return false;
			}
		}
		else {
			themePathPrefix = "struts/themes/";
		}
	}
	$('#jquery_theme_link').attr('href','../../'+themePathPrefix+newTheme+'/jquery-ui.css');
}

function getJSON() {
	$.ajax({
		url : "/gtl/spring/test.json",
		context : document.body
	}).done(function(datos) {
		$("#json_result").text(JSON.stringify(datos));
	});
}