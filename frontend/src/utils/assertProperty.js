export default function assertProperty (object, property) {
  if (!object.hasOwnProperty(property)) {
    throw Error(object + " has no property " + property);
  }
}
