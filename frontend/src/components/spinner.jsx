import "./spinner.css";

/**
 * https://loading.io/css/
 */
export default function Spinner({ fixedCentered }) {
  let classes = "lds-ellipsis";
  if (fixedCentered) {
    classes = classes + " spinner-fixed-centered";
  }
  return (
    <div className={classes}>
      <div></div>
      <div></div>
      <div></div>
      <div></div>
    </div>
  );
}
