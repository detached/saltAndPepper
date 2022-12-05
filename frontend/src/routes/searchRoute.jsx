import { useCallback, useEffect, useReducer, useState } from "react";
import { useTranslation } from "react-i18next";
import RecipeList from "../components/recipeList";
import { Page, SaltAndPepper, SearchRequest } from "../api/saltAndPepper";
import "./searchRoute.css";
import Spinner from "../components/spinner";

const SearchRequestActions = {
  SET_PAGE_NUMBER: "SET_PAGE_NUMBER",
  SET_QUERY: "SET_QUERY",
};

function searchRequestReducer(state, action) {
  switch (action?.type) {
    case SearchRequestActions.SET_PAGE_NUMBER:
      return {
        searchQuery: state.searchQuery,
        page: new Page(state.page.size, action.pageNumber),
      };
    case SearchRequestActions.SET_QUERY:
      return {
        searchQuery: action.searchQuery,
        page: state.page,
      };
    default:
      return state;
  }
}

export default function SearchRoute() {
  const { t } = useTranslation();

  const [searchRequest, dispatchSearchRequest] = useReducer(
    searchRequestReducer,
    {
      searchQuery: "",
      page: new Page(20, 0),
    },
    searchRequestReducer
  );

  const [searchResult, setSearchResult] = useState({
    items: [],
    page: new Page(0, 0),
  });

  const [isLoading, setIsLoading] = useState(false);
  const [doSearch, setDoSearch] = useState(true);

  const submitSearchQuery = useCallback(
    (event) => {
      event.preventDefault();
      if (!doSearch) {
        setDoSearch(true);
      }
    },
    [doSearch]
  );

  const switchPage = useCallback(
    (pageNumber) => {
      dispatchSearchRequest({
        type: SearchRequestActions.SET_PAGE_NUMBER,
        pageNumber: pageNumber,
      });
      setDoSearch(true);
    },
    [dispatchSearchRequest, setDoSearch]
  );

  useEffect(() => {
    if (isLoading || !doSearch) {
      return;
    }

    setIsLoading(true);

    SaltAndPepper.search(
      new SearchRequest(searchRequest.searchQuery, searchRequest.page)
    )
      .then((result) => {
        setSearchResult({
          page: result.page,
          items: result.data,
        });
      })
      .finally(() => {
        setIsLoading(false);
        setDoSearch(false);
      });
  }, [isLoading, doSearch, searchRequest, setSearchResult]);

  return (
    <div className="pure-g">
      {isLoading ? <Spinner fixedCentered="true" /> : null}
      <div className="pure-u-1-1">
        <form
          className="pure-form search-mask"
          onSubmit={(e) => submitSearchQuery(e)}
        >
          <input
            id="query"
            type="search"
            name="query"
            value={searchRequest.searchQuery}
            onChange={(e) =>
              dispatchSearchRequest({
                type: SearchRequestActions.SET_QUERY,
                searchQuery: e.target.value,
              })
            }
          />
          <input
            id="submit"
            type="submit"
            value={t("search.submit")}
            disabled={isLoading}
            className="pure-button pure-button-primary"
          />
        </form>
      </div>
      <div className="pure-u-1-1">
        <RecipeList
          page={searchResult.page}
          listItems={searchResult.items}
          setPage={switchPage}
        />
      </div>
    </div>
  );
}
