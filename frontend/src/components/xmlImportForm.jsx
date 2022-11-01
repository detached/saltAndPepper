import {useTranslation} from "react-i18next";
import {useDropzone} from "react-dropzone";
import "./xmlImportForm.css";
import {useCallback, useState} from "react";
import {SaltAndPepper} from "../api/saltAndPepper";
import {OperationState} from "../model/OperationState";

export default function XmlImportForm() {

    const {t} = useTranslation();
    const {acceptedFiles, getRootProps, getInputProps} = useDropzone({
        accept: {'text/xml': ['.xml']},
        multiple: false
    });
    const [isLoading, setIsLoading] = useState(false);
    const [importState, setImportState] = useState(OperationState.NONE);
    const [errorMessage, setErrorMessage] = useState("");

    const uploadFiles = useCallback(e => {
        e.preventDefault();
        if (isLoading || acceptedFiles.length === 0) {
            return;
        }
        setIsLoading(true);

        SaltAndPepper.import("gourmet", acceptedFiles[0]).then(() => {
            setImportState(OperationState.SUCCESSFUL);
        }).catch((e) => {
            setImportState(OperationState.ERROR);
            setErrorMessage(e.message);
        }).finally(() => {
            setIsLoading(false);
        })
    }, [acceptedFiles, isLoading, setIsLoading]);

    function importResult() {
        switch (importState) {
            case OperationState.SUCCESSFUL:
                return <p className="import-result">{t("import.success")}</p>;
            case OperationState.ERROR:
                return <p className="import-result">{t("import.error")}: {errorMessage}</p>;
            default:
                return null;
        }
    }

    return <>
        <h3>{t("import.gourmet")}</h3>
        <section className="container">
            <div {...getRootProps({className: 'dropzone'})}>
                <input {...getInputProps()} />
                <p>{t("import.dropzone")}</p>
            </div>
            {acceptedFiles.length !== 0 ?
                <aside className="file">
                    {t("import.dropzone.file")}: {acceptedFiles[0].name}
                </aside> : null}
        </section>
        <button className="pure-button import-button" onClick={e => uploadFiles(e)}
                disabled={acceptedFiles.length === 0 || importState === OperationState.SUCCESSFUL}>
            {t("import.submit")}
        </button>
        {importResult()}
    </>
}