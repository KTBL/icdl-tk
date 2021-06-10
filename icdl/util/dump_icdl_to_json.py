#! /bin/python3
import json
from os import listdir
from os.path import isfile

import hcl2


def clean_json(json_data):
    return {
        "AnimalCategory": clean__AnimalCategory(json_data["AnimalCategory"]),
        "Indicator": clean__Indicator(json_data["Indicator"])
    }


def clean_keyed(data, keyed_elements, singletons, list_elements):
    """
    Be aware: Nested recursion ahead!
    """
    cleaned = []
    for elem in data:
        for outer_key, outer_val in elem.items():
            outer_cleaned = clean(outer_val, keyed_elements, singletons, list_elements)
            outer_cleaned["__key"] = outer_key
            cleaned.append(outer_cleaned)
    return cleaned


def clean(data, keyed_elements, singletons, list_elements):
    """
    Be aware: Recursion ahead!

    data:
    keyed_elements: Named Stanza. Elements which have a stanza name, and appear in lists. The stanza name will be
                    moved into it's object as "__key". This way the following

                    "Aufzuchtkalb": {
                        "Branch": [
                          {
                            "branchLabel1": {
                              "name": "Gesamtbestand",
                              "description": "Beschreibung des Branch"
                            }
                          }
                        ]
                    }

                    will be transformed to

                    "Aufzuchtkalb": {
                        "Branch": [
                          {
                            "name": "Gesamtbestand",
                            "description": "Beschreibung des Branch"
                            "__key" "branchLabel1"
                          }
                        ]
                    }

                    if "Branch" is a keyed element

    singletons:     Unnamed Stanza. Singletons are elements which are within an own stanza, without being named and
                    which appear only once. Examples are "Evaluation" and "Assessment". These are marked, so they
                    will be moved out of a may generated list. Example:

                    "Assessment": [
                      {
                        "default_type": "By_Indicator",
                        "type_changeable": false,
                        "presentation": "counter",
                      }
                    ]

                    becomes

                    "Assessment": {
                        "default_type": "By_Indicator",
                        "type_changeable": false,
                        "presentation": "counter",
                    }

    list_elements:  Unnamed Stanza. List elements are like singletons, but instead of only appearing once these are
                    lists.
    """
    cleaned = dict()
    for key, value in data.items():
        if key in keyed_elements:
            cleaned[key] = clean_keyed(value, keyed_elements, singletons, list_elements)
        elif key in singletons:
            cleaned[key] = clean(value[0], keyed_elements, singletons, list_elements)
        elif key in list_elements:
            cleaned[key] = list()
            for element in value:
                tmp = clean(element, keyed_elements, singletons, list_elements)
                cleaned[key].append(tmp)
        else:
            cleaned[key] = single_value_of(key, value)
    return cleaned


def clean__AnimalCategory(data):
    return clean_keyed(data, ("Branch",), set(), set())


def single_value_of(key, value):
    if type(value) is list:
        if len(value) > 1:
            raise ValueError(f"Error with multiple elements in list, expected one! {key}: {value}")
        else:
            if len(value) == 1:
                return value[0]
            else:
                return None
    else:
        return value


def clean__Indicator(data):
    return clean_keyed(data, ("Var", "Range"), ("Assessment", "Evaluation", "Options",),
                       ("Group", "Formula", "Ranges", "ComposedVar"))


def load_hcl(path):
    with open(path, 'r', encoding='utf8') as fd:
        hcl_dict = hcl2.load(fd)

    return hcl_dict


def dump_json(hcl_dict, out_path):
    json_dict = json.dumps(hcl_dict, ensure_ascii=False)
    with open(out_path, 'w', encoding="utf8") as fd:
        fd.write(json_dict)


def parse_clean_dump(path):
    loaded = load_hcl(path)
    cleaned = clean_json(loaded)
    dump_json(cleaned, f"{path}.json")


if __name__ == '__main__':
    icdl_files = [f for f in listdir() if isfile(f) and not f.endswith('.json')]
    for f in icdl_files:
        print(f"{f} ... ", end='')
        parse_clean_dump(f)
        print(f"✔")
