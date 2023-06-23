import "./newAuthors.css";
import { IconContext } from "react-icons";
import { FaUserPlus } from "react-icons/fa";

export default function NewAuthors() {

    return (
        <IconContext.Provider value={{ size: "2em" }}>
            <div className="newauthors-box">
                    <div className="newauthors-card">
                        <div id="image"><FaUserPlus /></div>
                        <div id="name">Sonja Steenhoff</div>
                        <div id="date">2022-01-01</div>
                    </div>
                    <div className="newauthors-card">
                        <div id="image"><FaUserPlus /></div>
                        <div id="name">Sonja Steenhoff</div>
                        <div id="date">2022-01-01</div>
                    </div>
                    <div className="newauthors-card">
                        <div id="image"><FaUserPlus /></div>
                        <div id="name">Sonja Steenhoff</div>
                        <div id="date">2022-01-01</div>
                    </div>
                    <div className="newauthors-card">
                        <div id="image"><FaUserPlus /></div>
                        <div id="name">Sonja Steenhoff</div>
                        <div id="date">2022-01-01</div>
                    </div>
                    <div className="newauthors-card">
                        <div id="image"><FaUserPlus /></div>
                        <div id="name">Sonja Steenhoff</div>
                        <div id="date">2022-01-01</div>
                    </div>
                    <div className="newauthors-card">
                        <div id="image"><FaUserPlus /></div>
                        <div id="name">Sonja Steenhoff</div>
                        <div id="date">2022-01-01</div>
                    </div>
            </div>
        </IconContext.Provider>
    )
}