import "./modal.css";

export default function Modal({
                                  headerText,
                                  contentText,
                                  buttonOneText,
                                  buttonTwoText,
                                  buttonOneCallback,
                                  buttonTwoCallback
                              }) {

    return (
        <div className="modal">
            <div className="model-inner">
                <div className="modal-header">
                    <h3>{headerText}</h3>
                </div>
                <p>{contentText}</p>
                <div className="modal-buttons">
                    <button className="pure-button pure-button-primary" onClick={buttonOneCallback}>
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