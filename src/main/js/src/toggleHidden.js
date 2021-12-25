export function toggleHidden(elementId) {
    var element = document.getElementById(elementId);
    var visibility = element.style.visibility;
    if (visibility == 'visible') {
       element.style.visibility = 'hidden';
    } else {
       element.style.visibility = 'visible';
    }
}