export function toggleHidden(elementId) {
    var element = document.getElementById(elementId);
    var visibility = element.style.visibility;
    if (visibility == 'visible') {
       element.style.visibility = 'hidden';
       element.style.height = '0px';
    } else {
       element.style.visibility = 'visible';
       element.style.height = 'auto';
    }
}