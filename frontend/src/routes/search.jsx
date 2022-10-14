import {useCallback, useState} from "react";
import {useTranslation} from "react-i18next";
import RecipeList from "../components/recipeList";
import {Page, SaltAndPepper, SearchRequest} from "../api/saltAndPepper";

export default function Search() {
    const {t} = useTranslation();
    const [searchQuery, setSearchQuery] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [page, setPage] = useState(new Page(0, 0));
    const [searchResults, setSearchResults] = useState([]);

    const submitSearchQuery = useCallback(
        (event) => {
            event.preventDefault();

            if (isLoading) {
                return;
            }
            setIsLoading(true);

            SaltAndPepper.search(
                new SearchRequest(searchQuery, page)
            ).then((result) => {
                setPage(result.page);
                setSearchResults(result.data);
            }).finally(() => {
                setIsLoading(false);
            })
        },
        [searchQuery, isLoading, page, setIsLoading, setPage, setSearchResults]
    );

    const switchPage = (pageNumber) => {
        setPage(new Page(page.size, pageNumber, page.maxNumber));
    }

    return (
        <>
            <div className="search-header">
                <form className="pure-form" onSubmit={(e) => submitSearchQuery(e)}>
                    <input
                        type="search"
                        id="query"
                        name="query"
                        className="pure-input-3-4"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                    <input
                        type="submit"
                        value={t("search.submit")}
                        disabled={isLoading}
                        className="pure-button pure-button-primary"
                    />
                </form>
            </div>
            <div className="content">
                <RecipeList page={page} listItems={searchResults} switchPageCallback={switchPage}/>
            </div>
        </>
    );
}
