import { useTranslation } from "react-i18next";
import { FilterKey } from "../api/saltAndPepper";
import { useState, useCallback, useReducer, useEffect } from "react";
import "./searchFilter.css";

const FilterStatesAction = {
  NEW_FILTER_VALUES: "NEW_FILTER_VALUES",
  SET_STATE: "SET_STATE",
};

function filterStatesReducer(state, action) {
  let mapPossibleFilterToCheckboxStates = (key) => {
    const newValues = action.possibleFilter[key];
    if (newValues) {
      let valueStates = {};
      newValues.forEach((filterValue) => {
        const existingState = state[key]
          ? state[key][filterValue.value]?.checked
          : null;

        valueStates[filterValue.value] = {
          label: filterValue.label,
          checked: existingState ? existingState : false,
        };
      });
      return valueStates;
    } else {
      return {};
    }
  };

  switch (action?.type) {
    case FilterStatesAction.NEW_FILTER_VALUES:
      let newFilterValueStates = {};
      newFilterValueStates[FilterKey.AUTHOR] =
        mapPossibleFilterToCheckboxStates(FilterKey.AUTHOR);
      newFilterValueStates[FilterKey.CATEGORY] =
        mapPossibleFilterToCheckboxStates(FilterKey.CATEGORY);
      newFilterValueStates[FilterKey.CUISINE] =
        mapPossibleFilterToCheckboxStates(FilterKey.CUISINE);
      return newFilterValueStates;
    case FilterStatesAction.SET_STATE:
      let newState = {};
      newState[FilterKey.AUTHOR] = state[FilterKey.AUTHOR];
      newState[FilterKey.CATEGORY] = state[FilterKey.CATEGORY];
      newState[FilterKey.CUISINE] = state[FilterKey.CUISINE];
      newState[action.key][action.value].checked = action.checked;
      return newState;
    default:
      return state;
  }
}

export function SearchFilter({ possibleFilter, onSelectedValueChanged }) {
  const { t } = useTranslation();
  const [activeMenu, setActiveMenu] = useState("");
  const [filterStates, dispatchFilterStates] = useReducer(
    filterStatesReducer,
    {}
  );

  useEffect(() => {
    dispatchFilterStates({
      type: FilterStatesAction.NEW_FILTER_VALUES,
      possibleFilter,
    });
  }, [possibleFilter]);

  useEffect(() => {
    function filterOnlySelectedValues(filterValues) {
      if (filterValues) {
        return Object.keys(filterValues).filter((value) => {
          return filterValues[value].checked;
        });
      } else {
        return [];
      }
    }
    let selectedValues = {};
    selectedValues[FilterKey.AUTHOR] = filterOnlySelectedValues(
      filterStates[FilterKey.AUTHOR]
    );
    selectedValues[FilterKey.CATEGORY] = filterOnlySelectedValues(
      filterStates[FilterKey.CATEGORY]
    );
    selectedValues[FilterKey.CUISINE] = filterOnlySelectedValues(
      filterStates[FilterKey.CUISINE]
    );
    onSelectedValueChanged(selectedValues);
  }, [filterStates, onSelectedValueChanged]);

  const toggleMenuActive = useCallback(
    (key) => {
      if (activeMenu === key) {
        setActiveMenu("");
      } else {
        setActiveMenu(key);
      }
    },
    [activeMenu, setActiveMenu]
  );

  const toggleCheckbox = useCallback(
    (key, value, checked) => {
      dispatchFilterStates({
        type: FilterStatesAction.SET_STATE,
        key,
        value,
        checked,
      });
    },
    [dispatchFilterStates]
  );

  function isItemInMenuSelected(key) {
    return Object.entries(filterStates[key]).some((item) => {
      return item[1].checked;
    });
  }

  function dropdown(key, values) {
    let menuClassNames = "pure-menu-item pure-menu-has-children";
    if (activeMenu === key) {
      menuClassNames = menuClassNames + " pure-menu-active";
    }

    let titleClassNames = "pure-menu-link searchfilter-menu-title";
    if (isItemInMenuSelected(key)) {
      titleClassNames = titleClassNames + " searfilter-menu-title-selected";
    }

    return (
      <li key={key} id={key} className={menuClassNames}>
        <p className={titleClassNames} onClick={() => toggleMenuActive(key)}>
          {t("search.filter." + key)}
        </p>
        <ul className="searchfilter-children-box pure-menu-children">
          {Object.entries(values).map((item) => {
            const filterValue = item[0];
            const filterState = item[1];
            return (
              <li className="pure-menu-item pure-form" key={filterValue}>
                <label>
                  <input
                    type="checkbox"
                    value={filterValue}
                    defaultChecked={filterState.checked}
                    onChange={(e) =>
                      toggleCheckbox(key, filterValue, e.target.checked)
                    }
                  />{" "}
                  {filterState.label}
                </label>
              </li>
            );
          })}
        </ul>
      </li>
    );
  }

  return (
    <div className="pure-menu pure-menu-horizontal">
      <ul className="pure-menu-list">
        {Object.entries(filterStates).map((item) => {
          const key = item[0];
          const values = item[1];
          return dropdown(key, values);
        })}
      </ul>
    </div>
  );
}
