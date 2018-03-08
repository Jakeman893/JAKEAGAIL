import numpy as np
import pandas as pd

def clean_data(df, fill_method='ffill'):
    '''
    Takes in a pandas DataFrame and changes all string attributes and outputs
    to integers. Returns the DataFrame and the mapping
    fill_method can be:
    - 'ffill': forward fill
    - 'bfill': backward fill
    '''
    # Find all columns where data type is string
    text_cols = df.select_dtypes(include=['object']).columns.values
    text_cols = list(text_cols)
    mappings = dict()

    if text_cols:
        def apply_mapping(index):
            text = df.loc[index][colname]
            num = colname_mapping[text]
            df.loc[index, colname] = num

        for colname in text_cols:
            view = df[colname]
            distinct = view.unique()

            colname_mapping = {text : num for num, text in enumerate(distinct)}
            mappings[colname] = colname_mapping
            map(apply_mapping, df.index)


    # Replace True/False with 0 or 1
    bool_cols = df.select_dtypes(include=['bool']).columns.values
    bool_cols = list(bool_cols)

    if bool_cols:
        for colname in bool_cols:
            df[colname] = df[colname].astype(int)

    # # Fill missing values using forward filling
    df = df.fillna(method=fill_method)

    return (df, mappings)


def process_dataset(filename, outname):
    df = pd.read_csv(filename)
    colnames = list(df.columns)

    attributes = df[colnames[:-1]].copy()
    outputs = df[colnames[-1]]

    attributes, mappings = clean_data(attributes)
    #
    df = pd.concat([attributes, outputs], axis=1)
    print df

    df.to_csv(outname, index=False)


if __name__ == '__main__':
    filename = 'src/opt/test/data.csv'  # Which file you're reading from
    outname = 'src/opt/test/data.csv'  # Name of the file to write to (erases if already exists)
    process_dataset(filename, outname)
