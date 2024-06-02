import "./modal.css";
import PropTypes from "prop-types";

export default function Modal({
  headerText,
  contentText,
  buttonOneText,
  buttonTwoText,
  buttonOneCallback,
  buttonTwoCallback,
}) {
  return (
    <div className="modal">
      <div className="model-inner">
        <div className="modal-header">
          <h3>{headerText}</h3>
        </div>
        <p>{contentText}</p>
        <div className="modal-buttons">
          <button
            className="pure-button pure-button-primary"
            onClick={buttonOneCallback}
          >
            {buttonOneText}
          </button>
          <button className="pure-button" onClick={buttonTwoCallback}>
            {buttonTwoText}
          </button>
        </div>
      </div>
    </div>
  );
}
Modal.propTypes = {
  headerText: PropTypes.string,
  contentText: PropTypes.string,
  buttonOneText: PropTypes.string,
  buttonTwoText: PropTypes.string,
  buttonOneCallback: PropTypes.func,
  buttonTwoCallback: PropTypes.func,
};
