import { useCallback, useEffect, useReducer, useState } from "react";
import { useTranslation } from "react-i18next";
import RecipeList from "../components/recipeList";
import {
  Page,
  SaltAndPepper,
  SearchRequest,
  SearchFilter,
  FilterKey,
  OrderField
} from "../api/saltAndPepper";
import "./searchRoute.css";
import Spinner from "../components/spinner";
import { SearchFilter as SearchFilterComponent } from "../components/searchFilter";

const SearchRequestActions = {
  SET_PAGE_NUMBER: "SET_PAGE_NUMBER",
  SET_QUERY: "SET_QUERY",
  SET_FILTER: "SET_FILTER",
};

function searchRequestReducer(state, action) {
  switch (action?.type) {
    case SearchRequestActions.SET_PAGE_NUMBER:
      return {
        searchQuery: state.searchQuery,
        page: new Page(state.page.size, action.pageNumber),
        filter: state.filter,
      };
    case SearchRequestActions.SET_QUERY:
      return {
        searchQuery: action.searchQuery,
        page: state.page,
        filter: state.filter,
      };
    case SearchRequestActions.SET_FILTER:
      return {
        searchQuery: state.searchQuery,
        page: state.page,
        filter: new SearchFilter(
          action.filter[FilterKey.AUTHOR],
          action.filter[FilterKey.CATEGORY],
          action.filter[FilterKey.CUISINE]
        ),
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
      filter: new SearchFilter(),
    },
    searchRequestReducer
  );

  const [searchResult, setSearchResult] = useState({
    items: [],
    page: new Page(0, 0),
    possibleFilter: new SearchFilter(),
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
      new SearchRequest(
        searchRequest.searchQuery,
        searchRequest.page,
        searchRequest.filter,
        OrderField.TITLE,
      )
    )
      .then((result) => {
        setSearchResult({
          page: result.page,
          items: result.data,
          possibleFilter: result.possibleFilter,
        });
      })
      .finally(() => {
        setIsLoading(false);
        setDoSearch(false);
      });
  }, [isLoading, doSearch, searchRequest, setSearchResult]);

  var handleOnSelectedFilterChanged = useCallback(
    (selectedFilter) => {
      dispatchSearchRequest({
        type: SearchRequestActions.SET_FILTER,
        filter: selectedFilter,
      });
    },
    [dispatchSearchRequest]
  );

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
        <SearchFilterComponent
          possibleFilter={searchResult.possibleFilter}
          onSelectedValueChanged={handleOnSelectedFilterChanged}
        />
        <RecipeList
          page={searchResult.page}
          listItems={searchResult.items}
          setPage={switchPage}
        />
      </div>
    </div>
  );
}
